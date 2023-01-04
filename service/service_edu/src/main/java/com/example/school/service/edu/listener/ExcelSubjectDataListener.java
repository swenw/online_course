package com.example.school.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.service.edu.entity.Subject;
import com.example.school.service.edu.entity.excel.ExcelSubjectData;
import com.example.school.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 全参构造函数
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    private static final int BATCH_COUNT = 100;
    /**
     * 遍历每一行记录
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {
        log.info("解析到一条记录：{}", data);
        // 处理读取出来的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();

        Subject byTitle = getByTitle(levelOneTitle); // 判断一级分类是否存在
        String parentId;
        if (byTitle == null) { // 将一级分类存入数据库
            Subject subject = new Subject();
            subject.setTitle(levelOneTitle);
            subject.setParentId("0");
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId = byTitle.getId();
        }
        // 判断相同一级分类下的二级分类是否重复
        Subject subByTitle = this.getSubByTitle(levelTwoTitle, parentId);
        if (subByTitle == null) {
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }
    }

    /**
     * 所有数据读取完成后的收尾工作
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * 根据一级分类的名称查询数据是否存在
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        Subject subject = subjectMapper.selectOne(queryWrapper);
        return subject;
    }

    /**
     * 根据分类的名称和父id查询分类是否存在
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return subjectMapper.selectOne(queryWrapper);
    }
}
