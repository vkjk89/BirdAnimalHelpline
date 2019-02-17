package org.birdhelpline.app.dataaccess;

import org.apache.commons.io.IOUtils;
import org.birdhelpline.app.model.CaseImage;
import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.CaseTxn;
import org.birdhelpline.app.utils.CaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Repository
public class CaseDao {
    private static final Logger logger = LoggerFactory.getLogger(CaseDao.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
    private static final String caseInfoByCaseIdQWithoutTxn = "select * from case_info where case_id = ?";
    private static final String caseInfoBySearchTermQWithoutTxn = "select * from case_info where case_id like :searchTerm";
    private static final String caseTxnsQ = "select * from case_txn where case_id = ?";
    private static final String casePendingForAckQ = "select DISTINCT ci.* from case_info ci, case_txn ct where ci.case_id=ct.case_id and ct.is_ack = 0 and ci.current_user_id = :userId and ct.to_user_id = :userId ";
    private static final String caseAcceptedByUserIdQ = "select DISTINCT ci.* from case_info ci, case_txn ct where ci.case_id=ct.case_id  and ct.is_ack = 1 and ct.to_user_id = :userId";
    private static final String caseInfoByUserIdQWithoutTxn = "select DISTINCT ci.* from case_info ci,case_txn ct where ci.case_id=ct.case_id  and ( ci.user_id_opened = :userId OR ci.user_id_closed = :userId OR ci.current_user_id = :userId )";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private UserDao userDao;

    final Supplier<RowMapper<CaseInfo>> caseInfoRowMapper = () -> (ResultSet rs, int i) -> {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setActive(rs.getBoolean("is_active"));
        caseInfo.setCaseId(rs.getLong("case_id"));
        caseInfo.setUserIdClosed(rs.getLong("user_id_closed"));
        caseInfo.setUserIdOpened(rs.getLong("user_id_opened"));
        caseInfo.setCurrentUserId(rs.getLong("current_user_id"));
        Timestamp creationDate = rs.getTimestamp("creation_date");
        caseInfo.setCreationDate(creationDate);
        LocalDateTime localDateTime = creationDate.toLocalDateTime();
        caseInfo.setCreationDateStr(formatter.format(localDateTime));
        caseInfo.setLastModificationDate(rs.getTimestamp("last_modification_date"));
        caseInfo.setCloseDate(rs.getTimestamp("close_date"));
        caseInfo.setDesc(rs.getString("description"));
        caseInfo.setCloseRemark(rs.getString("close_remark"));
        caseInfo.setTypeAnimal(rs.getString("animal_type"));
        caseInfo.setAnimalName(rs.getString("animal_name"));
        caseInfo.setAnimalCondition(rs.getString("animal_condition"));
        caseInfo.setLocation(rs.getString("location"));
        caseInfo.setLocationPincode(rs.getString("location_pincode"));
        caseInfo.setContactName(rs.getString("contact_name"));
        caseInfo.setContactNumber(rs.getString("contact_number"));
        caseInfo.setContactPrefix(rs.getString("contact_number_prefix"));
        caseInfo.setLocationLandMark(rs.getString("location_landmark"));
        caseInfo.setBirdOrAnimal(userDao.isBird(caseInfo.getTypeAnimal()) ? "Bird" : "Animal");
        caseInfo.setIsAck(rs.getInt("is_ack"));
        return caseInfo;
    };

    public CaseInfo getCaseInfoByCaseId(Long caseId) {
        List<CaseInfo> caseInfos;
        try {
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
                caseTxn.setIsAck(rs.getInt("is_ack"));
                caseTxn.setAmount(rs.getDouble("amount"));
                caseTxn.setCaseId(rs.getLong("case_id"));
                caseTxn.setFromUserId(rs.getLong("from_user_id"));
                caseTxn.setToUserId(rs.getLong("to_user_id"));
                caseTxn.setStatus(rs.getString("status"));
                caseTxn.setTransferDate(rs.getTimestamp("transfer_date"));
                if(caseTxn.getTransferDate() != null) {
                    caseTxn.setTransferDateStr(formatter.format(caseTxn.getTransferDate().toLocalDateTime()));
                }
                return caseTxn;
            });

        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return list;
    }

    public List<CaseInfo> getCaseInfoByUserId(Long userId, Boolean pendingForAck) {
        List<CaseInfo> caseInfos;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            caseInfos = namedParameterJdbcTemplate.query(
                    pendingForAck == null ? caseInfoByUserIdQWithoutTxn : pendingForAck ? casePendingForAckQ : caseAcceptedByUserIdQ, params
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
            String q = "insert into case_info (user_id_opened,animal_type,current_user_id,is_active,animal_name,animal_condition,contact_name,contact_number,location,location_pincode,location_landmark,contact_number_prefix) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    q,
                    new String[]{"case_id"});

            ps.setLong(1, caseInfo.getUserIdOpened());
            ps.setString(2, caseInfo.getTypeAnimal());
            ps.setLong(3, caseInfo.getCurrentUserId());
            ps.setBoolean(4, true);
            ps.setString(5, caseInfo.getAnimalName());
            ps.setString(6, caseInfo.getAnimalCondition());
            ps.setString(7, caseInfo.getContactName());
            ps.setString(8, caseInfo.getContactNumber());
            ps.setString(9, caseInfo.getLocation());
            ps.setString(10, caseInfo.getLocationPincode());
            ps.setString(11, caseInfo.getLocationLandMark());
            ps.setString(12, caseInfo.getContactPrefix());
            return ps;
        }), keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void saveCaseTxn(CaseInfo caseInfo) {
        this.jdbcTemplate.update((connection -> {
            String q = "insert into case_txn (case_id, from_user_id, status) values (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    q);

            ps.setLong(1, caseInfo.getCaseId());
            ps.setLong(2, caseInfo.getUserIdOpened());
            ps.setString(3, CaseStatus.OPEN.toString());
            return ps;
        }));
    }

    public void assignCase(Long userId, Long toUserId, Long caseId, String description, Double amount, String transferDate) {
        Timestamp transferCloseDate = getTransferCloseDate(transferDate);
        Double amountIncurred = amount == null ? 0.0:amount;
        this.jdbcTemplate.update((connection -> {
            String q = "update case_info set current_user_id = ? where case_id = ?";
            PreparedStatement ps = connection.prepareStatement(
                    q);

            ps.setLong(1, toUserId);
            ps.setLong(2, caseId);
            return ps;
        }));

        this.jdbcTemplate.update((connection -> {
            String q = "insert into case_txn (case_id, from_user_id, to_user_id,description,amount,transfer_date) values (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    q);

            ps.setLong(1, caseId);
            ps.setLong(2, userId);
            ps.setLong(3, toUserId);
            ps.setString(4, description);
            ps.setDouble(5, amountIncurred);
            ps.setTimestamp(6, transferCloseDate);
            return ps;
        }));
    }

    public void closeCase(Long userId, Long caseId, String closeRemark, String closeReason, Double chargesIncurred, String closeDate) {
        Timestamp closeDateToUse = getTransferCloseDate(closeDate);
        Double amountIncurred = chargesIncurred == null ? 0.0:chargesIncurred;
        this.jdbcTemplate.update((connection -> {
            String q = "update case_info set current_user_id = NULL, is_active = 0, user_id_closed = ? , close_remark = ? , animal_condition = ?, close_date = ? where case_id = ?";
            PreparedStatement ps = connection.prepareStatement(
                    q);
            ps.setLong(1, userId);
            ps.setString(2, closeRemark);
            ps.setString(3, closeReason);
            ps.setTimestamp(4, closeDateToUse);
            ps.setLong(5, caseId);
            return ps;
        }));


        this.jdbcTemplate.update((connection -> {
            String q = "insert into case_txn (case_id, from_user_id, to_user_id,status,amount) values (?,?,NULL,?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    q);

            ps.setLong(1, caseId);
            ps.setLong(2, userId);
            ps.setString(3, CaseStatus.CLOSED.toString());
            ps.setDouble(4,amountIncurred);
            return ps;
        }));
    }

    private Timestamp getTransferCloseDate(String closeDate) {
        Timestamp closeDateToUse = new Timestamp(new Date().getTime());
        if (closeDate != null) {
//                closeDateToUse = new Timestamp(FORMATTED_DATE_FORMAT.parse(closeDate).getTime());
            closeDateToUse =  Timestamp.valueOf(LocalDateTime.parse(closeDate,formatter));
        }
        return closeDateToUse;
    }

    public List<CaseInfo> getAllCaseInfoBySearchTerm(String searchTerm) {
        List<CaseInfo> caseInfos = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("searchTerm", "%" + searchTerm + "%");
            caseInfos = namedParameterJdbcTemplate.query(
                    caseInfoBySearchTermQWithoutTxn, params
                    , caseInfoRowMapper.get()
            );
        } catch (EmptyResultDataAccessException ex) {
            //throw new ObjectRetrievalFailureException(User.class, userName);
            return null;
        }
        return caseInfos;
    }

    public void saveNewBirdAnimal(String birdOrAnimal, String newBirdAnimal) {
        this.jdbcTemplate.update((connection -> {
            String insertQ = "insert into bird_animal (bird_animal_name,type) values (?,?)";
            PreparedStatement ps = connection.prepareStatement(
                    insertQ);
            ps.setString(1, newBirdAnimal);
            ps.setString(2, birdOrAnimal.equalsIgnoreCase("Animal") ? "A" : "B");
            return ps;
        }));
    }

    public void saveCaseImages(CaseImage caseImage) {
        for (int i = 0; i < caseImage.getImages().size(); i++) {
            int j = i;
            this.jdbcTemplate.update((connection -> {
                String insertQ = "insert into case_image (case_id,image) values (?,?)";
                PreparedStatement ps = connection.prepareStatement(
                        insertQ);
                ps.setLong(1, caseImage.getCaseId());
                ps.setBlob(2, new ByteArrayInputStream(caseImage.getImages().get(j)));
                return ps;
            }));
        }
    }

    public void loadCaseImages(CaseImage caseImage) {
        try {
            String query = "select image from case_image where case_id = ?";
            List<Blob> list = jdbcTemplate.queryForList(query, new Object[]{caseImage.getCaseId()}, Blob.class);
            for (Blob blob : list) {
                try {
                    caseImage.addImage(IOUtils.toByteArray(blob.getBinaryStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }


    public void updateCaseTxn(Long caseId, Long userId, boolean acceptReject) {
        this.jdbcTemplate.update((connection -> {
            String query = "update case_txn set is_ack = ? where case_id = ? and to_user_id =?";
            PreparedStatement ps = connection.prepareStatement(
                    query);

            ps.setLong(1, acceptReject ? 1 : -1);
            ps.setLong(2, caseId);
            ps.setLong(3, userId);
            return ps;
        }));

        this.jdbcTemplate.update((connection -> {
            String query = "update case_info set is_ack = ? where case_id = ?";
            PreparedStatement ps = connection.prepareStatement(
                    query);

            ps.setLong(1, acceptReject ? 1 : -1);
            ps.setLong(2, caseId);
            return ps;
        }));
    }

    public void updateUserInfo(Long userId, boolean acceptReject) {
        this.jdbcTemplate.update((connection -> {
            String query;
            if (acceptReject) {
                query = "update user_info set case_accepted_count=case_accepted_count+1 where user_id = ?";
            } else {
                query = "update user_info set case_rejected_count=case_rejected_count+1 where user_id = ?";
            }
            PreparedStatement ps = connection.prepareStatement(
                    query);

            ps.setLong(1, userId);
            return ps;
        }));

    }
}