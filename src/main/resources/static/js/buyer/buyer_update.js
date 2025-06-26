document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/buyer/info', {
            headers: { 'Accept': 'application/json' }
        });
        const result = await response.json();

        if (result.success) {
            const buyer = result.data;
            document.getElementById('email').value = buyer.email;
            document.getElementById('name').value = buyer.name;
            document.getElementById('nickname').value = buyer.nickname;
            document.getElementById('phone').value = buyer.phone;
        } else {
            alert(result.message);
            window.location.href = '/buyer/login';
        }
    } catch (error) {
        console.error('Error fetching buyer info:', error);
        alert('회원 정보를 불러오는 중 오류가 발생했습니다.');
    }
});

document.getElementById('update-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const updateData = {
        currentPassword: document.getElementById('current-password').value,
        newPassword: document.getElementById('new-password').value,
        nickname: document.getElementById('nickname').value,
        phone: document.getElementById('phone').value
    };

    try {
        const response = await fetch('/buyer/info', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        const result = await response.json();

        if (response.ok && result.success) {
            alert('회원정보가 성공적으로 수정되었습니다.');
            window.location.href = '/buyer/mypage';
        } else {
            alert('정보 수정 실패: ' + result.message);
        }
    } catch (error) {
        console.error('Error updating buyer info:', error);
        alert('회원정보 수정 중 오류가 발생했습니다.');
    }
});

document.getElementById('cancel-btn').addEventListener('click', () => {
    window.history.back(); // 이전 페이지로
}); 