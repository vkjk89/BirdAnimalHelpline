package org.birdhelpline.app.dataaccess;

import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.CaseTxn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Repository
public class CaseDao {
    Logger logger = LoggerFactory.getLogger(CaseDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //private String caseInfoByCaseIdQWithTxn ="select * from case_info ci , case_txn ct where ci.case_id =ct.case_id and ci.case_id = :caseId";
    private String caseInfoByCaseIdQWithoutTxn = "select * from case_info where case_id = ?";

    //private String caseInfoByUserIdQWithTxn = "select * from case_info ci , case_txn ct where ci.case_id =ct.case_id and (ci.user_id_opened = :userId OR ci.user_id_closed = :userId OR ci.current_user_id = :userId)";
    private String caseInfoByUserIdQWithoutTxn = "select * from case_info where user_id_opened = :userId OR user_id_closed = :userId OR current_user_id = :userId";
    private String caseTxnsQ = "select * from case_txn where case_id = ?";

    final Supplier<RowMapper<CaseInfo>> caseInfoRowMapper = () -> (ResultSet rs, int i) -> {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setActive(rs.getBoolean("is_active"));
        caseInfo.setCaseId(rs.getLong("case_id"));
        caseInfo.setUserIdClosed(rs.getLong("user_id_closed"));
        caseInfo.setUserIdOpened(rs.getLong("user_id_opened"));
        caseInfo.setCurrentUserId(rs.getLong("current_user_id"));
        caseInfo.setCreationDate(rs.getTimestamp("creation_date"));
        caseInfo.setLastModificationDate(rs.getTimestamp("last_modification_date"));
        caseInfo.setCloseDate(rs.getTimestamp("close_date"));
        caseInfo.setDesc(rs.getString("description"));
        caseInfo.setCloseRemark(rs.getString("close_remark"));
        caseInfo.setTypeAnimal(rs.getString("type_animal"));
        return caseInfo;
    };
    private String insertCaseQ;

    public CaseInfo getCaseInfoByCaseId(Long caseId) {
        List<CaseInfo> caseInfos = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("caseId", caseId);

            caseInfos = jdbcTemplate.query(caseInfoByCaseIdQWithoutTxn, new Object[]{caseId}
                    , caseInfoRowMapper.get()
            );
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        if (caseInfos != null && caseInfos.size() == 1) {
            return caseInfos.get(0);
        }
        return null;
    }

    public List<CaseTxn> getCaseTxn(Long caseId) {
        List<CaseTxn> list = null;
        try {
            list = jdbcTemplate.query(conn -> {
                PreparedStatement ps = conn.prepareStatement(caseTxnsQ);
                ps.setLong(1, caseId);
                return ps;
            }, (ResultSet rs, int i) -> {
                CaseTxn caseTxn = new CaseTxn();
                caseTxn.setAcked(rs.getBoolean("is_ack"));
                caseTxn.setAmount(rs.getDouble("amount"));
                caseTxn.setCaseId(rs.getLong("case_id"));
                caseTxn.setFromUserId(rs.getLong("from_user_id"));
                caseTxn.setToUserId(rs.getLong("to_user_id"));
                caseTxn.setStatus(rs.getString("status"));
                caseTxn.setTransferDate(rs.getTimestamp("transfer_date"));
                return caseTxn;
            });

        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return list;
    }

    public List<CaseInfo> getCaseInfoByUserId(Long userId) {
        List<CaseInfo> caseInfos = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            caseInfos = namedParameterJdbcTemplate.query(
                    caseInfoByUserIdQWithoutTxn, params
                    , caseInfoRowMapper.get()
            );
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return caseInfos;
    }

    public Long save(CaseInfo caseInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update((connection -> {
            insertCaseQ = "insert into case_info (user_id_opened,type_animal,current_user_id,is_active,animal_name,animal_condition,contact_name,contact_number,location,location_pincode) values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    insertCaseQ,
                    new String[]{"case_id"});

            ps.setLong(1, caseInfo.getUserIdOpened());
            ps.setString(2, caseInfo.getTypeAnimal());
            ps.setLong(3, caseInfo.getCurrentUserId());
            ps.setBoolean(4,true );
            ps.setString(5,caseInfo.getAnimalName());
            ps.setString(6,caseInfo.getAnimalCondition());
            ps.setString(7,caseInfo.getContactName());
            ps.setString(8,caseInfo.getContactNumber());
            ps.setString(9,caseInfo.getLocation());
            ps.setString(10,caseInfo.getLocationPincode());
            return ps;
        }), keyHolder);
        return keyHolder.getKey().longValue();
    }
}