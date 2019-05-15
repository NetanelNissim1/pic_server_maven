package app.pic.server.common.exceptions;

public class IllegalParameterException extends Exception{

    public IllegalParameterException(){
        super();
    }

    public IllegalParameterException( String message ){
        super( message );
    }

    public IllegalParameterException( String parameterName, Object parameterValue ){
        super( "Parameter " + parameterName + " with value=" + getStringValue( parameterValue ) + " is illegal" );
    }

    private static String getStringValue( Object parameterValue ){
        String value;
        if( parameterValue == null ){
            value = "null";
        } else {
            value = parameterValue.toString();
        }
        return value;
    }
} 
