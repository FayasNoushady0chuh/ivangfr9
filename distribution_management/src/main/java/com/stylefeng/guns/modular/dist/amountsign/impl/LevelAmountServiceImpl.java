package com.stylefeng.guns.modular.dist.amountsign.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.common.annotion.DataSource;
import com.stylefeng.guns.common.constant.DSEnum;
import com.stylefeng.guns.common.constant.dist.ProTypeStatus;
import com.stylefeng.guns.common.constant.dist.SituationStatus;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.dao.DisAmountSituationMapper;
import com.stylefeng.guns.common.persistence.dao.DisMemberAmountMapper;
import com.stylefeng.guns.common.persistence.dao.SysDicMapper;
import com.stylefeng.guns.common.persistence.model.DisAmountSituation;
import com.stylefeng.guns.common.persistence.model.DisMemberAmount;
import com.stylefeng.guns.common.persistence.model.DisMemberInfo;
import com.stylefeng.guns.common.persistence.model.SysDic;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.dist.amountsign.AmountService;
import com.stylefeng.guns.modular.dist.dao.DisMemberAmountDao;
import com.stylefeng.guns.modular.dist.service.IDisMemberInfoService;
import com.stylefeng.guns.modular.dist.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class LevelAmountServiceImpl implements AmountService {

    private DisAmountSituationMapper disAmountSituationMapper =  SpringContextHolder.getBean(DisAmountSituationMapper.class);

    private SysDicMapper sysDicMapper =  SpringContextHolder.getBean(SysDicMapper.class);

    private DisMemberAmountMapper disMemberAmountMapper = SpringContextHolder.getBean(DisMemberAmountMapper.class);

    IDisMemberInfoService disMemberInfoService  = SpringContextHolder.getBean(IDisMemberInfoService.class) ;

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void addMoney(String userId, BigDecimal amount, String sourceName, String type) {

        DisMemberAmount disMemberAmount=new DisMemberAmount();
        disMemberAmount.setDisUserId(userId);
        DisMemberAmount memberAmount=disMemberAmountMapper.selectOne(disMemberAmount);
        //记录金额
        DisAmountSituation situation=new DisAmountSituation();
        situation.setDisUserId(userId);
        situation.setBeforeChangeAmount(memberAmount.getTotalAmount());
        situation.setChangeAmount(amount);
        BigDecimal avaibleAmount=memberAmount.getAvaibleAmount();
        BigDecimal totalAmount=memberAmount.getTotalAmount();
        memberAmount.setAvaibleAmount(avaibleAmount.add(amount));
        memberAmount.setTotalAmount(totalAmount.add(amount));

        situation.setAfterChangeAmount(memberAmount.getTotalAmount());
        BigDecimal afterThirdAmount=new BigDecimal(0);
        BigDecimal beforeThirdAmount=new BigDecimal(0);

        /*每次需要改变的地方*/
        afterThirdAmount=memberAmount.getLevelTotalAmount().add(amount);
        beforeThirdAmount=memberAmount.getLevelTotalAmount();
        memberAmount.setLevelTotalAmount(memberAmount.getLevelTotalAmount().add(amount));
        memberAmount.setLevelAvaibleAmount(memberAmount.getLevelAvaibleAmount().add(amount));
        /*每次需要改变的地方*/

        situation.setSpecificBeforeChangeAmount(beforeThirdAmount);
        situation.setSpecificAfterChangeAmount(afterThirdAmount);
        situation.setType(SituationStatus.INCOME_STATUS.getStatus());
        situation.setAddTime(DateUtils.longToDateAll(System.currentTimeMillis()));




        situation.setAccountType(ProTypeStatus.ONE_STATUS.getStatus());
        disMemberAmountMapper.updateById(memberAmount);
        Wrapper<DisAmountSituation> situationWrapper=new EntityWrapper<>();
        situationWrapper.eq("dis_user_id",userId)
                .eq("account_type",ProTypeStatus.ONE_STATUS.getStatus());
        Integer count=disAmountSituationMapper.selectCount(situationWrapper);
        if(count == 0){
            DisMemberInfo memberInfo= disMemberInfoService.selectListByUserId(userId);
            DisAmountSituation initSituation=new DisAmountSituation();
            initSituation.setAccountType(ProTypeStatus.ONE_STATUS.getStatus());
            initSituation.setDisUserId(userId);
            initSituation.setAddTime(memberAmount.getAddTime());
            initSituation.setDescribe(SituationStatus.AMOUNT_INIT.getDes());
            initSituation.setType(SituationStatus.AMOUNT_INIT.getStatus());
            initSituation.setChangeAmount(new BigDecimal(0));
            disAmountSituationMapper.insert(initSituation);
        }
        String des=SituationStatus.INCOME_STATUS.getDes();
        String lastDes = String.format(des,sourceName,ProTypeStatus.ONE_STATUS.getCode(),userId,amount.toString());
        situation.setDescribe(lastDes);
        disAmountSituationMapper.insert(situation);

    }

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void frozenAmount(String userId, BigDecimal amount) {
        DisMemberAmount disMemberAmount=new DisMemberAmount();
        disMemberAmount.setDisUserId(userId);
        DisMemberAmount memberAmount=disMemberAmountMapper.selectOne(disMemberAmount);
        BigDecimal  avaibleThirdAmount=new BigDecimal(0);
        avaibleThirdAmount=memberAmount.getLevelAvaibleAmount();
        if(avaibleThirdAmount.compareTo(amount)==-1){
            throw  new BussinessException(BizExceptionEnum.LOW_MONEY);
        }
        /*开始扣除金额*/
        memberAmount.setAvaibleAmount(memberAmount.getAvaibleAmount().subtract(amount));
        memberAmount.setFrozenAmount(memberAmount.getFrozenAmount().add(amount));
        memberAmount.setLevelAvaibleAmount(memberAmount.getLevelAvaibleAmount().subtract(amount));
        memberAmount.setLevelFrozenAmount(memberAmount.getLevelFrozenAmount().add(amount));
        disMemberAmountMapper.updateById(memberAmount);
    }

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void reduceMoney(String userId, BigDecimal amount) {
        String accountType = ProTypeStatus.ONE_STATUS.getCode();
        //记录金额
        DisAmountSituation situation=new DisAmountSituation();
        situation.setDisUserId(userId);
        situation.setChangeAmount(amount);

        DisMemberAmount disMemberAmount=new DisMemberAmount();
        disMemberAmount.setDisUserId(userId);
        DisMemberAmount memberAmount=disMemberAmountMapper.selectOne(disMemberAmount);
        situation.setBeforeChangeAmount(memberAmount.getTotalAmount());
        situation.setAfterChangeAmount(memberAmount.getTotalAmount().subtract(amount));
        BigDecimal  avaibleThirdAmount=new BigDecimal(0);

        avaibleThirdAmount=memberAmount.getLevelFrozenAmount();


        if(avaibleThirdAmount.compareTo(amount)==-1){
            throw  new BussinessException(BizExceptionEnum.LOW_MONEY);
        }
        /*开始扣除金额*/
        memberAmount.setTotalAmount(memberAmount.getTotalAmount().subtract(amount));
        memberAmount.setFrozenAmount(memberAmount.getFrozenAmount().subtract(amount));
        BigDecimal beforeThirdAmount=new BigDecimal(0);


        beforeThirdAmount=memberAmount.getLevelTotalAmount();
        memberAmount.setTradeFrozenAmount(memberAmount.getTradeFrozenAmount().subtract(amount));
        memberAmount.setLevelTotalAmount(beforeThirdAmount.subtract(amount));

        situation.setSpecificBeforeChangeAmount(beforeThirdAmount);
        situation.setSpecificAfterChangeAmount(beforeThirdAmount.subtract(amount));
        situation.setType(SituationStatus.PAY_STATUS.getStatus());
        situation.setAddTime(DateUtils.longToDateAll(System.currentTimeMillis()));
        SysDic sysDicParam=new SysDic();
        sysDicParam.setDicTypeNo("accountType");
        sysDicParam.setDicNotes(accountType);
        SysDic sysDic=sysDicMapper.selectOne(sysDicParam);
        situation.setAccountType(sysDic.getDicNo());
        disMemberAmountMapper.updateById(memberAmount);
        String des=SituationStatus.PAY_STATUS.getDes();
        situation.setDescribe(String.format(des,userId,accountType));
        disAmountSituationMapper.insert(situation);
    }

    @Override
    @DataSource(name = DSEnum.DATA_SOURCE_BIZ)
    public void returnMoney(String userId, BigDecimal amount) {
        DisMemberAmount disMemberAmount=new DisMemberAmount();
        disMemberAmount.setDisUserId(userId);
        DisMemberAmount memberAmount=disMemberAmountMapper.selectOne(disMemberAmount);
        BigDecimal  avaibleThirdAmount=new BigDecimal(0);

        avaibleThirdAmount=memberAmount.getLevelFrozenAmount();

        if(avaibleThirdAmount.compareTo(amount)==-1){
            throw  new BussinessException(BizExceptionEnum.LOW_MONEY);
        }
        /*开始增加金额*/
        memberAmount.setAvaibleAmount(memberAmount.getAvaibleAmount().add(amount));
        memberAmount.setFrozenAmount(memberAmount.getFrozenAmount().subtract(amount));

        memberAmount.setLevelAvaibleAmount(memberAmount.getLevelAvaibleAmount().add(amount));
        memberAmount.setLevelFrozenAmount(memberAmount.getLevelFrozenAmount().subtract(amount));

        disMemberAmountMapper.updateById(memberAmount);
    }
}
