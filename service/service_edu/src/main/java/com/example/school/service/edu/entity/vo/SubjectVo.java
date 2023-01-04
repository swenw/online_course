package com.example.school.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Integer sort;
    // 双向关联，需定义其父亲
    // private SubjectVo parent;
    private List<SubjectVo> children = new ArrayList<>();

}
