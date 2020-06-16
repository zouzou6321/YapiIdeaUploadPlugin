package com.qbb.dto;

import com.qbb.vo.InterfaceVo;

import java.util.List;

/**
 * @Description:
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 * @Company: 成都国盛天丰技术有限责任公司
 * @Author: 程立涛
 * @CreateDate: 2019/9/29 15:54
 * @UpdateDate: 2019/9/29 15:54
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 根据分类id查询接口列表返回结果
 */
public class YapiListByCatResponse {

    private Integer count;
    private Integer total;

    private List<InterfaceVo> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<InterfaceVo> getList() {
        return list;
    }

    public void setList(List<InterfaceVo> list) {
        this.list = list;
    }
}
