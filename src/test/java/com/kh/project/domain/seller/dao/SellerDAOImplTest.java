package com.kh.project.domain.seller.dao;

import com.kh.project.domain.entity.Seller;
import com.kh.project.domain.entity.MemberGubun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SellerDAO ?µí© ?ì¤?? */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "logging.level.com.kh.project=DEBUG",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
@Transactional
@DisplayName("SellerDAO ?µí© ?ì¤??)
class SellerDAOImplTest {

    @Autowired
    private SellerDAO sellerDAO;

    private Seller testSeller;

    @BeforeEach
    void setUp() {
        testSeller = createSampleSeller();
    }

    private Seller createSampleSeller() {
        Seller seller = new Seller();
        seller.setEmail("seller@shop.com");
        seller.setPassword("ShopPass123!");
        seller.setBizRegNo("111-22-33333");
        seller.setShopName("?ì¤?¸ì??);
        seller.setName("?ë§¤?ì´ë¦?);
        seller.setShopAddress("ë¶?°ì ?¨êµ¬ ??°ë¡");
        seller.setTel("010-1234-5678");
        seller.setGubun(MemberGubun.NEW.getCode());
        seller.setStatus("ACTIVE");
        return seller;
    }

    // ==================== ????ì¤??====================

    @Test
    @DisplayName("?ë§¤?????- ?±ê³µ")
    void save_success() {
        // when
        Seller savedSeller = sellerDAO.save(testSeller);

        // then
        assertNotNull(savedSeller);
        assertNotNull(savedSeller.getSellerId());
        assertEquals(testSeller.getEmail(), savedSeller.getEmail());
        assertEquals(testSeller.getBizRegNo(), savedSeller.getBizRegNo());
        assertEquals(testSeller.getShopName(), savedSeller.getShopName());
        assertNotNull(savedSeller.getCdate());
    }

    @Test
    @DisplayName("?ë§¤?????- ?ì ?ë ?ë½???¤í¨")
    void save_fail_missing_required_fields() {
        // given: ?´ë©?¼ì´ null???ë§¤??        testSeller.setEmail(null);

        // when & then: ?ì¸ ë°ì ?ì
        assertThrows(Exception.class, () -> {
            sellerDAO.save(testSeller);
        });
    }

    // ==================== ì¡°í ?ì¤??====================

    @Test
    @DisplayName("IDë¡??ë§¤??ì¡°í - ?±ê³µ")
    void findById_success() {
        // given: ?ë§¤?????        Seller savedSeller = sellerDAO.save(testSeller);

        // when: IDë¡?ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findById(savedSeller.getSellerId());

        // then: ì¡°í ?±ê³µ ?ì¸
        assertTrue(foundSeller.isPresent());
        assertEquals(savedSeller.getSellerId(), foundSeller.get().getSellerId());
        assertEquals(savedSeller.getEmail(), foundSeller.get().getEmail());
        assertEquals(savedSeller.getShopName(), foundSeller.get().getShopName());
    }

    @Test
    @DisplayName("IDë¡??ë§¤??ì¡°í - ì¡´ì¬?ì? ?ë ID")
    void findById_not_found() {
        // when: ì¡´ì¬?ì? ?ë IDë¡?ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findById(999999L);

        // then: ì¡°í ê²°ê³¼ ?ì
        assertFalse(foundSeller.isPresent());
    }

    @Test
    @DisplayName("?´ë©?¼ë¡ ?ë§¤??ì¡°í - ?±ê³µ")
    void findByEmail_success() {
        // given: ?ë§¤?????        sellerDAO.save(testSeller);

        // when: ?´ë©?¼ë¡ ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findByEmail(testSeller.getEmail());

        // then: ì¡°í ?±ê³µ ?ì¸
        assertTrue(foundSeller.isPresent());
        assertEquals(testSeller.getEmail(), foundSeller.get().getEmail());
    }

    @Test
    @DisplayName("?´ë©?¼ë¡ ?ë§¤??ì¡°í - ì¡´ì¬?ì? ?ë ?´ë©??)
    void findByEmail_not_found() {
        // when: ì¡´ì¬?ì? ?ë ?´ë©?¼ë¡ ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findByEmail("notfound@seller.com");

        // then: ì¡°í ê²°ê³¼ ?ì
        assertFalse(foundSeller.isPresent());
    }

    @Test
    @DisplayName("?¬ì?ë±ë¡ë²?¸ë¡ ?ë§¤??ì¡°í - ?±ê³µ")
    void findByBizRegNo_success() {
        // given: ?ë§¤?????        sellerDAO.save(testSeller);

        // when: ?¬ì?ë±ë¡ë²?¸ë¡ ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findByBizRegNo(testSeller.getBizRegNo());

        // then: ì¡°í ?±ê³µ ?ì¸
        assertTrue(foundSeller.isPresent());
        assertEquals(testSeller.getBizRegNo(), foundSeller.get().getBizRegNo());
    }

    @Test
    @DisplayName("?¬ì?ë±ë¡ë²?¸ë¡ ?ë§¤??ì¡°í - ì¡´ì¬?ì? ?ë ë²í¸")
    void findByBizRegNo_not_found() {
        // when: ì¡´ì¬?ì? ?ë ?¬ì?ë±ë¡ë²?¸ë¡ ì¡°í
        Optional<Seller> foundSeller = sellerDAO.findByBizRegNo("999-88-77777");

        // then: ì¡°í ê²°ê³¼ ?ì
        assertFalse(foundSeller.isPresent());
    }

    // ==================== ?ë°?´í¸ ?ì¤??====================

    @Test
    @DisplayName("?ë§¤???ë³´ ?ì  - ?±ê³µ")
    void update_success() {
        // given: ?ë§¤?????        Seller savedSeller = sellerDAO.save(testSeller);

        // when: ?ë³´ ?ì 
        Seller updateSeller = new Seller();
        updateSeller.setShopName("?ì ?ì?ëª");
        updateSeller.setShopAddress("ë¶?°ì ?´ì´?êµ?);
        updateSeller.setTel("010-9999-8888");

        int updatedRows = sellerDAO.update(savedSeller.getSellerId(), updateSeller);

        // then: ?ì  ?±ê³µ ?ì¸
        assertEquals(1, updatedRows);

        // ?ì ???°ì´??ê²ì¦?        Optional<Seller> updatedSellerOpt = sellerDAO.findById(savedSeller.getSellerId());
        assertTrue(updatedSellerOpt.isPresent());
        Seller updatedSellerData = updatedSellerOpt.get();
        assertEquals("?ì ?ì?ëª", updatedSellerData.getShopName());
        assertEquals("ë¶?°ì ?´ì´?êµ?, updatedSellerData.getShopAddress());
        assertEquals("010-9999-8888", updatedSellerData.getTel());
    }

    @Test
    @DisplayName("?ë§¤???ë³´ ?ì  - ì¡´ì¬?ì? ?ë ID")
    void update_not_found() {
        // given: ?ì ???°ì´??        Seller updateSeller = new Seller();
        updateSeller.setShopName("?ì ?ì?ëª");

        // when: ì¡´ì¬?ì? ?ë IDë¡??ì  ?ë
        int updatedRows = sellerDAO.update(999999L, updateSeller);

        // then: ?ì ?ì? ?ì
        assertEquals(0, updatedRows);
    }

    // ==================== ?í´ ?ì¤??====================

    @Test
    @DisplayName("?ë§¤???í´ ì²ë¦¬ - ?±ê³µ")
    void withdrawWithReason_success() {
        // given: ?ë§¤?????        Seller savedSeller = sellerDAO.save(testSeller);

        // when: ?í´ ì²ë¦¬
        String reason = "?¬ì ì¢ë£";
        int withdrawnRows = sellerDAO.withdrawWithReason(savedSeller.getSellerId(), reason);

        // then: ?í´ ?±ê³µ ?ì¸
        assertEquals(1, withdrawnRows);

        // ?í´ ?í ?ì¸
        Optional<Seller> withdrawnSellerOpt = sellerDAO.findById(savedSeller.getSellerId());
        assertTrue(withdrawnSellerOpt.isPresent());
        Seller withdrawnSeller = withdrawnSellerOpt.get();
        assertEquals("WITHDRAWN", withdrawnSeller.getStatus());
        assertNotNull(withdrawnSeller.getWithdrawnAt());
        assertEquals(reason, withdrawnSeller.getWithdrawnReason());
    }

    @Test
    @DisplayName("?ë§¤???í´ ì²ë¦¬ - ì¡´ì¬?ì? ?ë ID")
    void withdrawWithReason_not_found() {
        // when: ì¡´ì¬?ì? ?ë IDë¡??í´ ?ë
        int withdrawnRows = sellerDAO.withdrawWithReason(999999L, "?í´ ?¬ì ");

        // then: ?í´?ì? ?ì
        assertEquals(0, withdrawnRows);
    }

    // ==================== ì¤ë³µ ì²´í¬ ?ì¤??====================

    @Test
    @DisplayName("?´ë©??ì¤ë³µ ì²´í¬ - ì¤ë³µ??)
    void existsByEmail_true() {
        // given: ?ë§¤?????        sellerDAO.save(testSeller);

        // when: ?´ë©??ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByEmail(testSeller.getEmail());

        // then: ì¤ë³µ??        assertTrue(exists);
    }

    @Test
    @DisplayName("?´ë©??ì¤ë³µ ì²´í¬ - ì¤ë³µ ?ë¨")
    void existsByEmail_false() {
        // when: ì¡´ì¬?ì? ?ë ?´ë©??ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByEmail("new@seller.com");

        // then: ì¤ë³µ ?ë¨
        assertFalse(exists);
    }

    @Test
    @DisplayName("?¬ì?ë±ë¡ë²??ì¤ë³µ ì²´í¬ - ì¤ë³µ??)
    void existsByBizRegNo_true() {
        // given: ?ë§¤?????        sellerDAO.save(testSeller);

        // when: ?¬ì?ë±ë¡ë²??ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByBizRegNo(testSeller.getBizRegNo());

        // then: ì¤ë³µ??        assertTrue(exists);
    }

    @Test
    @DisplayName("?¬ì?ë±ë¡ë²??ì¤ë³µ ì²´í¬ - ì¤ë³µ ?ë¨")
    void existsByBizRegNo_false() {
        // when: ì¡´ì¬?ì? ?ë ?¬ì?ë±ë¡ë²??ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByBizRegNo("999-88-77777");

        // then: ì¤ë³µ ?ë¨
        assertFalse(exists);
    }

    @Test
    @DisplayName("?í¸ëª?ì¤ë³µ ì²´í¬ - ì¤ë³µ??)
    void existsByShopName_true() {
        // given: ?ë§¤?????        sellerDAO.save(testSeller);

        // when: ?í¸ëª?ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByShopName(testSeller.getShopName());

        // then: ì¤ë³µ??        assertTrue(exists);
    }

    @Test
    @DisplayName("?í¸ëª?ì¤ë³µ ì²´í¬ - ì¤ë³µ ?ë¨")
    void existsByShopName_false() {
        // when: ì¡´ì¬?ì? ?ë ?í¸ëª?ì¤ë³µ ì²´í¬
        boolean exists = sellerDAO.existsByShopName("?ì?ëª");

        // then: ì¤ë³µ ?ë¨
        assertFalse(exists);
    }

    // ==================== ëª©ë¡ ì¡°í ?ì¤??====================

    @Test
    @DisplayName("?ì²´ ?ë§¤??ëª©ë¡ ì¡°í")
    void findAll() {
        // given: ?¬ë¬ ?ë§¤?????        sellerDAO.save(testSeller);

        Seller seller2 = createSampleSeller();
        seller2.setEmail("seller2@shop.com");
        seller2.setBizRegNo("222-33-44444");
        seller2.setShopName("?ë²ì§¸ì??);
        sellerDAO.save(seller2);

        // when: ?ì²´ ëª©ë¡ ì¡°í
        List<Seller> sellers = sellerDAO.findAll();

        // then: ??¥ë ?ë§¤?ë¤??ì¡°í??        assertNotNull(sellers);
        assertTrue(sellers.size() >= 2);
        assertTrue(sellers.stream().anyMatch(s -> s.getEmail().equals(testSeller.getEmail())));
        assertTrue(sellers.stream().anyMatch(s -> s.getEmail().equals(seller2.getEmail())));
    }

    @Test
    @DisplayName("?í´???ë§¤??ëª©ë¡ ì¡°í")
    void findWithdrawnMembers() {
        // given: ?ë§¤????????í´ ì²ë¦¬
        Seller savedSeller = sellerDAO.save(testSeller);
        sellerDAO.withdrawWithReason(savedSeller.getSellerId(), "?ì¤???í´");

        // when: ?í´???ë§¤??ëª©ë¡ ì¡°í
        List<Seller> withdrawnSellers = sellerDAO.findWithdrawnMembers();

        // then: ?í´???ë§¤?ê? ì¡°í??        assertNotNull(withdrawnSellers);
        assertTrue(withdrawnSellers.stream().anyMatch(s -> 
            s.getSellerId().equals(savedSeller.getSellerId()) && 
            "WITHDRAWN".equals(s.getStatus())
        ));
    }

    // ==================== Edge Case ?ì¤??====================

    @Test
    @DisplayName("??ë¬¸??êµ¬ë¶ ?´ë©??ì¤ë³µ ì²´í¬")
    void existsByEmail_case_sensitivity() {
        // given: ?ë¬¸???´ë©?¼ë¡ ???        sellerDAO.save(testSeller);

        // when: ?ë¬¸ìë¡?ì¤ë³µ ì²´í¬
        boolean existsUpper = sellerDAO.existsByEmail(testSeller.getEmail().toUpperCase());
        boolean existsLower = sellerDAO.existsByEmail(testSeller.getEmail().toLowerCase());

        // then: ??ë¬¸??êµ¬ë¶ ?ì¸
        assertTrue(existsLower); // ?ë³¸ê³??ì¼
        // existsUpper??DB ?¤ì ???°ë¼ ?¤ë¦
    }

    @Test
    @DisplayName("?¹ìë¬¸ì ?¬í¨ ?í¸ëª?ì²ë¦¬")
    void handle_special_characters_in_shop_name() {
        // given: ?¹ìë¬¸ì ?¬í¨ ?í¸ëª?        testSeller.setShopName("?ì¤?¸ì??#$%");

        // when: ???ë°?ì¡°í
        Seller savedSeller = sellerDAO.save(testSeller);
        Optional<Seller> foundSeller = sellerDAO.findById(savedSeller.getSellerId());

        // then: ?¹ìë¬¸ì ? ì? ?ì¸
        assertTrue(foundSeller.isPresent());
        assertEquals("?ì¤?¸ì??#$%", foundSeller.get().getShopName());
    }

    @Test
    @DisplayName("?¬ì?ë±ë¡ë²???ì ?ì¤??)
    void bizRegNo_format_test() {
        // given: ?¤ì???ì???¬ì?ë±ë¡ë²??        testSeller.setBizRegNo("123-45-67890");

        // when: ???        Seller savedSeller = sellerDAO.save(testSeller);

        // then: ?ì ? ì? ?ì¸
        assertEquals("123-45-67890", savedSeller.getBizRegNo());
    }

    @Test
    @DisplayName("null ê°?ì²ë¦¬ ?ì¤??)
    void handle_null_values() {
        // when & then: null ?´ë©?¼ë¡ ì¤ë³µ ì²´í¬
        assertThrows(Exception.class, () -> {
            sellerDAO.existsByEmail(null);
        });

        // when & then: null IDë¡?ì¡°í
        assertThrows(Exception.class, () -> {
            sellerDAO.findById(null);
        });

        // when & then: null ?¬ì?ë±ë¡ë²?¸ë¡ ì¤ë³µ ì²´í¬
        assertThrows(Exception.class, () -> {
            sellerDAO.existsByBizRegNo(null);
        });
    }

    @Test
    @DisplayName("?ì ????ì¤??- ?´ë©??ì¤ë³µ")
    void concurrent_save_test_email_duplicate() {
        // given: ?ì¼???´ë©?¼ì ê°ì§????ë§¤??        Seller seller1 = createSampleSeller();
        Seller seller2 = createSampleSeller();
        seller2.setBizRegNo("222-33-44444"); // ?¬ì?ë²?¸ë ?¤ë¥´ê²?        seller2.setShopName("?¤ë¥¸?ì ëª?); // ?ì ëªë ?¤ë¥´ê²?
        // when: ì²?ë²ì§¸ ???        sellerDAO.save(seller1);

        // then: ??ë²ì§¸ ??¥ì ?´ë©??ì¤ë³µ ?ë¬
        assertThrows(Exception.class, () -> {
            sellerDAO.save(seller2);
        });
    }

    @Test
    @DisplayName("?ì ????ì¤??- ?¬ì?ë±ë¡ë²??ì¤ë³µ")
    void concurrent_save_test_bizRegNo_duplicate() {
        // given: ?ì¼???¬ì?ë±ë¡ë²?¸ë? ê°ì§????ë§¤??        Seller seller1 = createSampleSeller();
        Seller seller2 = createSampleSeller();
        seller2.setEmail("different@email.com"); // ?´ë©?¼ì? ?¤ë¥´ê²?        seller2.setShopName("?¤ë¥¸?ì ëª?); // ?ì ëªë ?¤ë¥´ê²?
        // when: ì²?ë²ì§¸ ???        sellerDAO.save(seller1);

        // then: ??ë²ì§¸ ??¥ì ?¬ì?ë±ë¡ë²??ì¤ë³µ ?ë¬
        assertThrows(Exception.class, () -> {
            sellerDAO.save(seller2);
        });
    }

    @Test
    @DisplayName("ë§¤ì° ê¸?ë¬¸ì??ì²ë¦¬ ?ì¤??)
    void handle_long_strings() {
        // given: ë§¤ì° ê¸??ì ëª?        String longShopName = "??.repeat(500); // ë§¤ì° ê¸??ì ëª?        testSeller.setShopName(longShopName);

        // when & then: ê¸¸ì´ ?í ?ì¸ (DB ì»¬ë¼ ê¸¸ì´???°ë¼)
        if (longShopName.length() > 255) { // ?¼ë°?ì¸ ë¬¸ì??ê¸¸ì´ ?í
            assertThrows(Exception.class, () -> {
                sellerDAO.save(testSeller);
            });
        }
    }
} 
