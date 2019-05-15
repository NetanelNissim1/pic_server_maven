package app.pic.server.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * CloseUtils
 */
public class CloseUtils{

    public static void closeSilently( AutoCloseable closeable ){
        if( closeable != null ){
            try{
                closeable.close();
            } catch ( Exception ignore ) {
            }
        }
    }

    public static void closeSilently( Closeable closeable ){
        if( closeable != null ){
            try{
                closeable.close();
            } catch ( IOException ignore ) {
            }
        }
    }

    public static void disconnectSilently( HttpURLConnection disconnectable ){
        if( disconnectable != null ){
            disconnectable.disconnect();
        }
    }

    public static void closeAndDisconnect( HttpURLConnection disconnectable, Closeable... closeables ){
        if( closeables != null && closeables.length > 0 ){
            for( Closeable closeable : closeables ){
                closeSilently( closeable );
            }
        }
        disconnectSilently( disconnectable );
    }
}
