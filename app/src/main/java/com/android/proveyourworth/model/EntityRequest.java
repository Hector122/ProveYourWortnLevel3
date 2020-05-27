package com.android.proveyourworth.model;

import java.io.File;

public class EntityRequest {

    private File image;
    private File resume;
    private File code;
    private String email, name, about;


    public EntityRequest(File image, File resume, File code, String email, String name, String about) {
        this.image = image;
        this.resume = resume;
        this.code = code;
        this.email = email;
        this.name = name;
        this.about = about;
    }

    public File getImage() {
        return image;
    }

    public File getResume() {
        return resume;
    }

    public File getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }
}
