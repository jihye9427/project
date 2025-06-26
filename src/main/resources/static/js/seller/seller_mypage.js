document.addEventListener('DOMContentLoaded', async () => {
    const welcomeMessage = document.getElementById('welcome-message');

    try {
        const response = await fetch('/seller/info', {
            headers: { 'Accept': 'application/json' }
        });
        const result = await response.json();

        if (result.success) {
            const seller = result.data;
            welcomeMessage.textContent = `${seller.name}님, 환영합니다.`;
            document.getElementById('seller-email').textContent = seller.email;
            document.getElementById('seller-name').textContent = seller.name;
            document.getElementById('seller-nickname').textContent = seller.nickname;
            document.getElementById('seller-phone').textContent = seller.phone;
            document.getElementById('seller-store-name').textContent = seller.storeName;
            document.getElementById('seller-business-number').textContent = seller.businessNumber;
        } else {
            alert(result.message);
            if (result.code === 'USER_NOT_AUTHENTICATED' || result.code === 'USER_NOT_FOUND') {
                window.location.href = '/seller/login';
            }
        }
    } catch (error) {
        console.error('Error fetching seller info:', error);
        alert('회원 정보를 불러오는 중 오류가 발생했습니다.');
        window.location.href = '/seller/login';
    }

    // 2. 버튼 이벤트 리스너
    document.getElementById('update-info-btn').addEventListener('click', () => {
        window.location.href = '/seller_update.html';
    });

    document.getElementById('leave-btn').addEventListener('click', () => {
        window.location.href = '/seller_leave.html';
    });

    document.getElementById('logout-btn').addEventListener('click', () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            fetch('/api/sellers/logout', { method: 'POST' })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    }
                    throw new Error('로그아웃에 실패했습니다.');
                })
                .then(data => {
                    if (data.header.code === 'SUCCESS') {
                        alert('로그아웃되었습니다.');
                        window.location.href = '/';
                    } else {
                        throw new Error(data.header.message || '로그아웃에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('로그아웃 실패:', error);
                    alert('로그아웃에 실패했습니다.');
                });
        }
    });
}); 