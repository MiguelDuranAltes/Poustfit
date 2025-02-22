package com.hackudc.poustfit_server.remote.imgur;

public class ImgurResponse {
    private int status;
    private boolean success;
    private ImgurData data;

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public ImgurData getData() { return data; }
    public void setData(ImgurData data) { this.data = data; }
}
