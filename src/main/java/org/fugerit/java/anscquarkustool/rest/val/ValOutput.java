package org.fugerit.java.anscquarkustool.rest.val;

import org.fugerit.java.emp.sm.service.ServiceResponse;

public class ValOutput extends ServiceResponse {

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
