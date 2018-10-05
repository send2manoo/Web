package com.qlc.fieldsense.rest;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

/**
 *
 * @author jyoti
 */
@XmlRootElement(name = "state")
@XmlAccessorType(XmlAccessType.NONE)
public class State {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "code")
    private String code;

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
