document.addEventListener('DOMContentLoaded', async () => {
    const welcomeMessage = document.getElementById('welcome-message');

    try {
        const response = await fetch('/buyer/info', {
            headers: {
                'Accept': 'application/json'
            }
        });
        const result = await response.json();

        if (result.success) {
            const buyer = result.data;
            document.getElementById('buyer-email').textContent = buyer.email;
            document.getElementById('buyer-name').textContent = buyer.name;
            document.getElementById('buyer-nickname').textContent = buyer.nickname;
            document.getElementById('buyer-phone').textContent = buyer.phone;
            welcomeMessage.textContent = `${buyer.name}님, 환영합니다.`;
        } else {
            alert(result.message);
            if (result.code === 'USER_NOT_AUTHENTICATED' || result.code === 'USER_NOT_FOUND') {
                window.location.href = '/buyer/login';
            }
        }
    } catch (error) {
        console.error('Error fetching buyer info:', error);
        alert('회원 정보를 불러오는 중 오류가 발생했습니다.');
        window.location.href = '/buyer/login';
    }

    // 2. 버튼 이벤트 리스너
    document.getElementById('update-info-btn').addEventListener('click', () => {
        window.location.href = '/buyer_update.html';
    });

    document.getElementById('leave-btn').addEventListener('click', () => {
        window.location.href = '/buyer_leave.html';
    });

    document.getElementById('logout-btn').addEventListener('click', () => {
        fetch('/api/buyers/logout', { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                alert(data.header.message);
                if (data.header.code === 'SUCCESS') {
                    window.location.href = '/index.html';
                }
            })
            .catch(error => {
                console.error('Logout failed:', error);
                alert('로그아웃 중 오류가 발생했습니다.');
            });
    });
}); 