package org.zyy.campusdemobackend.dto;

public class MessageDTO {
    private Long from;
    private Long to;
    private String text;
    private String time;

    public Long getFrom() { return from; }
    public void setFrom(Long from) { this.from = from; }
    public Long getTo() { return to; }
    public void setTo(Long to) { this.to = to; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}