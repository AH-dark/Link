package com.ahdark.code.link.controller.api.root;

import com.ahdark.code.link.service.ShortLinkService;
import com.ahdark.code.link.service.UserService;
import com.ahdark.code.link.utils.ApiResult;
import com.ahdark.code.link.utils.CodeInfo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/root/stat")
@Slf4j
public class RootStatController {
    @Autowired
    ShortLinkService shortLinkService;
    @Autowired
    UserService userService;

    @GetMapping(path = "")
    public JSONObject GetLatestData(@RequestParam(value = "day", required = false) String day) {
        ResultData resultData = new ResultData();
        resultData.totalShortLink = this.shortLinkService.getNum();
        resultData.totalUser = this.userService.getNum();
        for (int i = 0, target = (day != null ? Integer.parseInt(day) : 7); i < target; i++) {
            Timestamp timestampStart = new Timestamp(NumberOfDaysStartUnixTime(i));
            Timestamp timestampEnd = new Timestamp(NumberOfDaysStartUnixTime(i - 1));
            log.info("Start: {}; End: {};", timestampStart.toLocalDateTime(), timestampEnd.toLocalDateTime());
            resultData.newShortLinkData.put(i, this.shortLinkService.getNum(timestampStart, timestampEnd));
            resultData.newUserData.put(i, this.userService.getNum(timestampStart, timestampEnd));
        }
        return new ApiResult<>(CodeInfo.SUCCESS, resultData).getJsonResult();
    }

    private static long NumberOfDaysStartUnixTime(int NumberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - NumberOfDays, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    static private class ResultData {
        public Map<Integer, Integer> newShortLinkData = new HashMap<>();
        public Map<Integer, Integer> newUserData = new HashMap<>();
        public Integer totalShortLink = 0;
        public Integer totalUser = 0;
    }
}
