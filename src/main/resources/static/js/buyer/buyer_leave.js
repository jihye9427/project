document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('buyer-leave-form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (!document.getElementById('agree').checked) {
            alert('탈퇴 약관에 동의해주세요.');
            return;
        }

        const password = document.getElementById('password').value;

        if (!confirm('정말로 탈퇴하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch('/buyer/leave', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ password: password })
            });

            const result = await response.json();

            if (response.ok && result.success) {
                alert('회원 탈퇴가 완료되었습니다.');
                window.location.href = '/'; // 메인 페이지로 이동
            } else {
                alert('회원 탈퇴 실패: ' + result.message);
            }
        } catch (error) {
            console.error('Error leaving:', error);
            alert('회원 탈퇴 처리 중 오류가 발생했습니다.');
        }
    });

    document.getElementById('cancel-btn').addEventListener('click', () => {
        window.location.href = '/buyer/mypage';
    });
}); 