package com.cybercloud.sprbotfreedom.web.entity.bo.log;

import lombok.Data;

/**
 * @author liuyutang
 * @date 2023/8/30
 */
@Data
public class OperatorLogDelBO {
   private String username;
   private String keywork;
   private String startTime;
   private String endTime;
   private String loginIp;
}
