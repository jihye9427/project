package com.kh.project.domain.seller.dao;

import com.kh.project.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SellerDAOImpl implements SellerDAO {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public Seller save(Seller seller) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("seller")
        .usingGeneratedKeyColumns("seller_id");

    BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(seller);
    Number key = jdbcInsert.executeAndReturnKey(params);
    seller.setSellerId(key.longValue());
    return seller;
  }

  @Override
  public Optional<Seller> findByEmail(String email) {
    String sql = "SELECT * FROM seller WHERE email = ?";
    try {
      Seller seller = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Seller.class), email);
      return Optional.of(seller);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Seller> findById(Long sellerId) {
    String sql = "SELECT * FROM seller WHERE seller_id = ?";
    try {
      Seller seller = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Seller.class), sellerId);
      return Optional.of(seller);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public int update(Long sellerId, Seller seller) {
    String sql = "UPDATE seller SET shop_name = ?, name = ?, shop_address = ?, tel = ? WHERE seller_id = ?";
    return jdbcTemplate.update(sql, seller.getShopName(), seller.getName(), seller.getShopAddress(), seller.getTel(), sellerId);
  }

  @Override
  public int delete(Long sellerId) {
    String sql = "UPDATE seller SET status = '비활성' WHERE seller_id = ?";
    return jdbcTemplate.update(sql, sellerId);
  }

  @Override
  public boolean existsByEmail(String email) {
    String sql = "SELECT count(*) FROM seller WHERE email = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
    return count != null && count > 0;
  }

  @Override
  public boolean existsByBizRegNo(String bizRegNo) {
    String sql = "SELECT count(*) FROM seller WHERE biz_reg_no = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bizRegNo);
    return count != null && count > 0;
  }
}
