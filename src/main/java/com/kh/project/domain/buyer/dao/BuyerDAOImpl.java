package com.kh.project.domain.buyer.dao;

import com.kh.project.domain.buyer.entity.Buyer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BuyerDAOImpl implements BuyerDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Buyer save(Buyer buyer) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("buyer")
                .usingGeneratedKeyColumns("buyer_id");
        
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(buyer);
        Number key = jdbcInsert.executeAndReturnKey(params);
        buyer.setBuyerId(key.longValue());
        return buyer;
    }

    @Override
    public Optional<Buyer> findByEmail(String email) {
        String sql = "SELECT * FROM buyer WHERE email = ?";
        try {
            Buyer buyer = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Buyer.class), email);
            return Optional.of(buyer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Buyer> findById(Long buyerId) {
        String sql = "SELECT * FROM buyer WHERE buyer_id = ?";
        try {
            Buyer buyer = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Buyer.class), buyerId);
            return Optional.of(buyer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public int update(Long buyerId, Buyer buyer) {
        String sql = "UPDATE buyer SET nickname = ?, tel = ?, address = ?, udate = systimestamp WHERE buyer_id = ?";
        return jdbcTemplate.update(sql, buyer.getNickname(), buyer.getTel(), buyer.getAddress(), buyerId);
    }

    @Override
    public int delete(Long buyerId) {
        String sql = "UPDATE buyer SET status = '비활성' WHERE buyer_id = ?";
        return jdbcTemplate.update(sql, buyerId);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT count(*) FROM buyer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        String sql = "SELECT count(*) FROM buyer WHERE nickname = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);
        return count != null && count > 0;
    }
}
