document.addEventListener('DOMContentLoaded', async () => {
    const welcomeMessage = document.getElementById('welcome-message');

    // 1. 판매자 정보 조회
    try {
        const response = await fetch('/api/sellers/info', {
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        if (result.success || result.code === "0") {
            const seller = result.data;
            welcomeMessage.textContent = `${seller.name || '판매자'}님, 환영합니다.`;
            document.getElementById('seller-email').textContent = seller.email || '';
            document.getElementById('seller-name').textContent = seller.name || '';
            document.getElementById('seller-bizRegNo').textContent = seller.bizRegNo || '';
            document.getElementById('seller-shopName').textContent = seller.shopName || '';
            document.getElementById('seller-shopAddress').textContent = seller.shopAddress || '';
            document.getElementById('seller-tel').textContent = seller.tel || '';
        } else {
            throw new Error(result.message || result.rtmsg || "판매자 정보를 불러올 수 없습니다.");
        }
    } catch (error) {
        console.error('판매자 정보 조회 오류:', error);
        alert('판매자 정보를 불러오는 중 오류가 발생했습니다: ' + error.message);
        window.location.href = '/seller/login';
    }

    // 2. 버튼 이벤트 리스너
    document.getElementById('update-info-btn').addEventListener('click', () => {
        window.location.href = '/seller/mypage';
    });

    document.getElementById('leave-btn').addEventListener('click', () => {
        window.location.href = '/seller/leave';
    });

    document.getElementById('logout-btn').addEventListener('click', async () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            try {
                const response = await fetch('/api/sellers/logout', {
                    method: 'POST'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();

                if (data.success || data.code === "0") {
                    alert('로그아웃되었습니다.');
                    window.location.href = '/';
                } else {
                    throw new Error(data.message || data.rtmsg || '로그아웃에 실패했습니다.');
                }
            } catch (error) {
                console.error('로그아웃 실패:', error);
                alert('로그아웃에 실패했습니다: ' + error.message);
            }
        }
    });
});
