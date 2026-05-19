package net.p5w.dp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.UfContract;
import net.p5w.dp.service.UfContractService;

@RestController
@RequestMapping("/api/contract")
public class UfContractController {

    @Autowired
    private UfContractService contractService;

    @GetMapping("/{id}")
    public Result<UfContract> get(@PathVariable Integer id) {
        return Result.success(contractService.selectByPrimaryKey(id));
    }
}
