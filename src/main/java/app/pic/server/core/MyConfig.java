package app.pic.server.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig{

    @Value("${live_cli_pic_dir}")
    private String LIVE_CLI_PIC_BASE_PATH;
    @Value("${cli_pic_dir}")
    private String LOCAL_CLI_PIC_BASE_PATH;

    public String getLIVE_CLI_PIC_BASE_PATH(){
        return LIVE_CLI_PIC_BASE_PATH;
    }

    public String getLOCAL_CLI_PIC_BASE_PATH(){
        return LOCAL_CLI_PIC_BASE_PATH;
    }
}
