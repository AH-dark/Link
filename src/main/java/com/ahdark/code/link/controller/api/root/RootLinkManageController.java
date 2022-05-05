package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.pojo.LimitData;
import com.ahdark.code.link.pojo.ShortLink;
import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.utils.ApiResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.ahdark.code.link.utils.CodeInfo.COMMON_FAIL;
import static com.ahdark.code.link.utils.CodeInfo.SUCCESS;

@RestController
@RequestMapping(path = "/api/root/link")
@Slf4j
public class RootLinkManageController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
    ShortLinkService shortLinkService;
    @Autowired
    HttpSession session;

    @GetMapping("/all")
    public JSONObject GetAll(
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "0", required = false) Integer user
    ) {
        log.info("GET all user event.");
        int userNum = this.shortLinkService.getNum(user);
        if (Math.max(0, (page - 1)) * limit > userNum) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        List<ShortLink> links = this.shortLinkService.getLatestShortLink(limit, Math.max(0, (page - 1)) * limit);
        if (links == null || links.isEmpty()) {
            return new ApiResult<>(COMMON_FAIL).getJsonResult();
        }

        LimitData<List<ShortLink>> limitData = new LimitData<>();
        limitData.setLimit(limit);
        limitData.setOffset(Math.max(0, (page - 1)) * limit);
        limitData.setTotal(userNum);
        limitData.setData(links);

        ApiResult<LimitData<List<ShortLink>>> result = new ApiResult<>(limitData);

        if (page * limit > userNum) {
            result.setExceptions("incomplete data");
        }

        log.info("Get Users success: {}", result);

        return result.getJsonResult();
    }

    @DeleteMapping(params = "key")
    public JSONObject Delete(@RequestParam String key) {
        Boolean isSuccess = shortLinkService.deleteShortLink(key);
        return new ApiResult<>(isSuccess ? SUCCESS : COMMON_FAIL).getJsonResult();
    }
}
