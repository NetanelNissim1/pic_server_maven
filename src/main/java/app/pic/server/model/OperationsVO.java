/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.pic.server.model;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "operations")
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationsVO extends ResourceSupport implements Serializable{

    private static final long serialVersionUID = 1L;

    public OperationsVO( String cli, String cliHeader, String type, String size ){
        super();
        this.cli = cli;
        this.cliHeader = cliHeader;
        this.type = type;
        this.size = size;
    }

    public OperationsVO(){
    }

    @NotEmpty(message = "cli must not be empty")
    private String cli;
    @NotEmpty(message = "cliHeader must not be empty")
    private String cliHeader;
    @NotEmpty(message = "type must not be empty")
    private String type;
    @NotEmpty(message = "size must not be empty")
    private String size;
    private int nFilesDeleted;

    private String[] message;

    public String[] getMessage(){
        return message;
    }

    public void setMessage( String... message ){
        this.message = message;
    }
    
    public int getnFilesDeleted(){
        return nFilesDeleted;
    }

    public void setnFilesDeleted( int nFilesDeleted ){
        this.nFilesDeleted = nFilesDeleted;
    }

    public String getCli(){
        return cli;
    }

    public void setCli( String cli ){
        this.cli = cli;
    }

    public String getCliHeader(){
        return cliHeader;
    }

    public void setCliHeader( String cliHeader ){
        this.cliHeader = cliHeader;
    }

    public String getType(){
        return type;
    }

    public void setType( String type ){
        this.type = type;
    }

    public String getSize(){
        return size;
    }

    public void setSize( String size ){
        this.size = size;
    }
}
