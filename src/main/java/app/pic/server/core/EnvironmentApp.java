package app.pic.server.core;

import app.pic.server.common.exceptions.IllegalParameterException;

public enum EnvironmentApp{
    LIVE,
    QA,
    LOCAL,
    OFFLINE_LIVE,
    OFFLINE_QA;

    public static EnvironmentApp getEnvironmentByStringValue( String env ) throws IllegalParameterException{
        if( env == null ){
            throw new IllegalParameterException( "Environment is not setted" );
        }

        for( EnvironmentApp curr : EnvironmentApp.values() ){
            if( curr.toString().equalsIgnoreCase( env ) ){
                return curr;
            }
        }

        throw new IllegalParameterException( "Unsupported environment: " + env );
    }
}
