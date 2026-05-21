package net.p5w.dp.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.UfContract;
import net.p5w.dp.service.UfContractService;

/**
 * 用友合同控制器
 */
@RestController
@RequestMapping("/api/contract")
public class UfContractController extends BaseController {

    @Resource
    private UfContractService contractService;

    /**
     * 根据主键查询合同详情
     *
     * @param id 合同主键
     * @return 合同详情
     */
    @GetMapping("/{id}")
    public Result<UfContract> get(@PathVariable Integer id) {
        return success(contractService.selectByPrimaryKey(id));
    }
}
