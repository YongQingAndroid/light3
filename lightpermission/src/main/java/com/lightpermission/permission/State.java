package com.lightpermission.permission;

public enum State {
    DONE(0),
    FAIL(1);
    int arg;
    String[] permission,fail;

    State(int arg) {
        this.arg = arg;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }

    public String[] getFail() {
        return fail;
    }

    public void setFail(String[] fail) {
        this.fail = fail;
    }

    public String[] getPermission() {
        return permission;
    }

    public int getValue() {
        return arg;
    }
}
