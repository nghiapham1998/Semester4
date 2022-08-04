package fpt.aptech.projectcard.domain;

public class Role {

    private Integer id;

    private ERole name;

    public Role(ERole name) {
        this.name = name;

    }

    public Role() {
        
    }

    public Role(Object o, ERole roleUser) {
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }
    public void setName(ERole name) {
        this.name = name;
    }

}
