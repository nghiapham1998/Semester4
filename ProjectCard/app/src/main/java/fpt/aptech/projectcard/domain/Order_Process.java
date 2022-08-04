package fpt.aptech.projectcard.domain;

public class Order_Process {

    private Long id;
    private String name;
    private String style;
    private String description;

    public Order_Process(String name, String style, String description) {
        this.name = name;
        this.style= style;
        this.description= description;
    }

    public Order_Process() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
