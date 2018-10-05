package com.qlc.fieldsense.rest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jyoti
 */
@Controller
@RequestMapping("/stateObject")
public class StateObjectController {

    /**
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public
    @ResponseBody
    State getState(@PathVariable String code) {
    //    System.out.println("Creating state object");
        State st = new State();

        if (code.equals("KL")) {
            st.setCode(code);
            st.setName("Kerala");
        } else {
            st.setCode(code);
            st.setName("Default State");
        }
        return st;

    }
}
