package com.cybercloud.sprbotfreedom.web.entity.bo.log;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyutang
 * @date 2023/8/30
 */
@Data
public class OperatorLogDelBO implements Serializable {
   private String username;
   private String keywork;
   private String startTime;
   private String endTime;
   private String loginIp;
}
