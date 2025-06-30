document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('seller-leave-form');

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const password = document.getElementById('password').value;

            if (!password) {
                alert('비밀번호를 입력해주세요.');
                return;
            }

            if (!confirm('정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
                return;
            }

            try {
                const response = await fetch('/api/sellers/leave', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ password })
                });

                const result = await response.json();

                if (result.rtcd === "0") {
                    alert(result.rtmsg || '탈퇴가 완료되었습니다.');
                    window.location.href = '/';
                } else {
                    throw new Error(result.rtmsg || "탈퇴 처리에 실패했습니다.");
                }
            } catch (error) {
                console.error('탈퇴 처리 오류:', error);
                alert('탈퇴 실패: ' + error.message);
            }
        });
    }
});