package app.pic.server.core;

import app.pic.server.common.exceptions.IllegalParameterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticContextInitializer implements InitializingBean, DisposableBean{

    @Autowired
    private MyConfig myConfig;
    public static EnvironmentApp ENVIRONMENT;
    private static final Logger LOGGER = LogManager.getLogger( StaticContextInitializer.class );
    public static String CLI_PIC_BASE_PATH;

    private void init(){
        try{
            ENVIRONMENT = EnvironmentApp.getEnvironmentByStringValue( System.getenv( "EYECON_PIC_SERVER_ENV" ) );
            LOGGER.info( "Environment=", ENVIRONMENT.toString() );
        } catch ( IllegalParameterException e ) {
            LOGGER.warn( "FATAL ERROR: System variable EYECON_PIC_SERVER_ENV is not setted. Application was NOT DEPLOYED. ", e.getMessage() );
            throw new ExceptionInInitializerError( e.getMessage() );
        }

        if( ENVIRONMENT == EnvironmentApp.LIVE ){
            StaticContextInitializer.CLI_PIC_BASE_PATH = myConfig.getLIVE_CLI_PIC_BASE_PATH();
        } else {
            StaticContextInitializer.CLI_PIC_BASE_PATH = myConfig.getLOCAL_CLI_PIC_BASE_PATH();
        }
        LOGGER.info( "\n================================================== SERVER WAS STARTED ==================================================" );
    }

    /**
     * Destroy server stop log and call to garbage collection.
     */
    private void shutdown(){
        long timeStamp = System.currentTimeMillis();
        try{
            LOGGER.info( "\n================================================== SERVER IS STOPPING ==================================================" );

            //to stop threadDeathWatcher      
            LOGGER.info( "Server STOPPED in: ", String.valueOf( System.currentTimeMillis() - timeStamp ) + "ms" );
            LOGGER.info( "\n================================================== SERVER WAS STOPPED ==================================================" );

        } finally {
            System.gc();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        init();
    }

    @Override
    public void destroy() throws Exception{
        shutdown();
    }
}
