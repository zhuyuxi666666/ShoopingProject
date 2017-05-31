package com.china.lhf.app.entity;

/**
 * Created by C罗 on 2016/9/14.
 */
public class Category extends BaseBean {
    private String name;

    public Category(){

    }

    public Category(String name){
          this.name=name;
    }

    public Category(Long id,String name){
           this.id=id;
           this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
