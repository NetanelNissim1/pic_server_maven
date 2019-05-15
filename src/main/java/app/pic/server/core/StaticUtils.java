package app.pic.server.core;

public class StaticUtils{

    private static MyConfig myConfig;

    public static void setMyConfig( MyConfig myConfig ){
        StaticUtils.myConfig = myConfig;
    }

    private StaticUtils(){
    }

    public static void printFoo(){
       // System.out.println( "Foo:" + myConfig.getFoo() );
    }
}
