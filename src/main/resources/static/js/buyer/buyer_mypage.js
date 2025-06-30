document.addEventListener('DOMContentLoaded', async () => {
    const welcomeMessage = document.getElementById('welcome-message');

    // 1. 회원 정보 조회
    try {
        const response = await fetch('/api/buyers/info', {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        if (result.success || result.code === "0") {
            const buyer = result.data;
            document.getElementById('buyer-email').textContent = buyer.email || '';
            document.getElementById('buyer-name').textContent = buyer.name || '';
            document.getElementById('buyer-nickname').textContent = buyer.nickname || '';
            document.getElementById('buyer-tel').textContent = buyer.tel || '';
            welcomeMessage.textContent = `${buyer.name || '회원'}님, 환영합니다.`;
        } else {
            throw new Error(result.message || result.rtmsg || "회원 정보를 불러올 수 없습니다.");
        }
    } catch (error) {
        console.error('회원 정보 조회 오류:', error);
        alert('회원 정보를 불러오는 중 오류가 발생했습니다: ' + error.message);
        window.location.href = '/buyer/login';
    }

    // 2. 버튼 이벤트 리스너
    document.getElementById('update-info-btn').addEventListener('click', () => {
        window.location.href = '/buyer/mypage';
    });

    document.getElementById('leave-btn').addEventListener('click', () => {
        window.location.href = '/buyer/leave';
    });

    document.getElementById('logout-btn').addEventListener('click', async () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            try {
                const response = await fetch('/api/buyers/logout', {
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
