package com.vanyne.reservation.application.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.repository.ReservationInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import com.vayne.model.common.Result;
import com.vayne.model.model.ListReservationInfoRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfoEntity> implements ReservationInfoService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ReservationInfoRepository reservationInfoRepository;

    /**
     * 分页查询会议室预约信息
     *
     * @param token              token
     * @param reservationInfoDto reservationInfoDto
     * @param pageNum            页码
     * @param pageSize           条数
     * @return list
     */
    @Override
    public ListReservationInfoRep listReservationInfo(String token, ReservationInfoDto reservationInfoDto,
                                                      Integer pageNum, Integer pageSize) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired, please log in again!", token);
            return new ListReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The token has expired, please log in again!")
                    );
        }
        // todo 分页查询数据库
        return null;
    }
}
