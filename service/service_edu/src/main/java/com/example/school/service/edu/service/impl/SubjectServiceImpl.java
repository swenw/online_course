package com.example.school.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.school.service.edu.entity.Subject;
import com.example.school.service.edu.entity.excel.ExcelSubjectData;
import com.example.school.service.edu.entity.vo.SubjectVo;
import com.example.school.service.edu.listener.ExcelSubjectDataListener;
import com.example.school.service.edu.mapper.SubjectMapper;
import com.example.school.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2022-11-21
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}
