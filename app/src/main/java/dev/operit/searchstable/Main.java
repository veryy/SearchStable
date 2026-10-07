package dev.operit.searchstable;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.lang.reflect.*;
import java.util.*;
public final class Main implements IXposedHookLoadPackage {
 static volatile boolean active;
 static int blocks;
 static final Map<Object,List> frozen=Collections.synchronizedMap(new WeakHashMap<Object,List>());
 static final Map<Object,Boolean> known=Collections.synchronizedMap(new WeakHashMap<Object,Boolean>());
 static Field data;
 static void log(String s){XposedBridge.log("SearchStable v0.3: "+s);}
 static List current(Object a)throws Throwable{return (List)data.get(a);}
 static List lock(Object a)throws Throwable{
  known.put(a,true);
  List s=frozen.get(a);
  if(s==null&&active&&!current(a).isEmpty()){
   s=new ArrayList(current(a));frozen.put(a,s);log("LOCK adapter count="+s.size()+" packages="+names(s));
  }
  return s;
 }
 static String names(List s){StringBuilder b=new StringBuilder();for(Object x:s){try{b.append(x.getClass().getMethod("getPackageName").invoke(x)).append(',');}catch(Throwable t){b.append('?');}}return b.toString();}
 static void restore(Object a,List s)throws Throwable{List c=current(a);if(!c.equals(s)){c.clear();c.addAll(s);log("RESTORE mutated order");}}
 public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lp)throws Throwable{
  if(!lp.packageName.equals("com.heytap.quicksearchbox"))return;
  Class<?> c=Class.forName("com.heytap.quicksearchbox.adapter.NewRecommendAppAdapter",false,lp.classLoader);
  data=c.getDeclaredField("c");data.setAccessible(true);
  for(final Method m:c.getDeclaredMethods()){
   final String n=m.getName();
   if(!(n.equals("P")||n.equals("J")||n.equals("E")||n.equals("B")||n.equals("onBindViewHolder")))continue;
   XposedBridge.hookMethod(m,new XC_MethodHook(){
    protected void beforeHookedMethod(MethodHookParam p)throws Throwable{
     known.put(p.thisObject,true);if(!active)return;
     List s=lock(p.thisObject);if(s==null)return;
     restore(p.thisObject,s);
     if(n.equals("P")||n.equals("J")){p.setResult(null);if(++blocks<=100)log("BLOCK "+n+" #"+blocks);return;}
     if(n.equals("E")||n.equals("B")){for(int i=0;i<p.args.length;i++)if(p.args[i] instanceof List)p.args[i]=new ArrayList(s);}
    }
    protected void afterHookedMethod(MethodHookParam p)throws Throwable{if(active&&!p.hasThrowable())lock(p.thisObject);}
   });log("hooked "+m.toString());
  }
  Class<?> a=Class.forName("android.app.Activity",false,lp.classLoader);
  XposedBridge.hookMethod(a.getDeclaredMethod("onResume"),new XC_MethodHook(){
   protected void beforeHookedMethod(MethodHookParam p)throws Throwable{if(!p.thisObject.getClass().getName().contains("SearchHomeActivity"))return;active=true;log("SESSION resume");Object[] aa; synchronized(known){aa=known.keySet().toArray();}for(Object x:aa)lock(x);}
  });
  XposedBridge.hookMethod(a.getDeclaredMethod("onPause"),new XC_MethodHook(){
   protected void afterHookedMethod(MethodHookParam p){if(!p.thisObject.getClass().getName().contains("SearchHomeActivity"))return;active=false;frozen.clear();log("UNLOCK pause");}
  });
  log("loaded; background updates allowed");
 }
}
