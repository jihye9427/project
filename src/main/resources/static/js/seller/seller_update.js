document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/seller/info', {
            headers: { 'Accept': 'application/json' }
        });
        const result = await response.json();

        if (result.success) {
            const seller = result.data;
            document.getElementById('email').value = seller.email;
            document.getElementById('name').value = seller.name;
            document.getElementById('nickname').value = seller.nickname;
            document.getElementById('phone').value = seller.phone;
            document.getElementById('store-name').value = seller.storeName;
        } else {
            alert(result.message);
            window.location.href = '/seller/login';
        }
    } catch (error) {
        console.error('Error fetching seller info:', error);
        alert('회원 정보를 불러오는 중 오류가 발생했습니다.');
    }
});

document.getElementById('update-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const updateData = {
        currentPassword: document.getElementById('current-password').value,
        newPassword: document.getElementById('new-password').value,
        nickname: document.getElementById('nickname').value,
        phone: document.getElementById('phone').value,
        storeName: document.getElementById('store-name').value,
    };

    try {
        const response = await fetch('/seller/info', {
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
            window.location.href = '/seller/mypage';
        } else {
            alert('정보 수정 실패: ' + result.message);
        }
    } catch (error) {
        console.error('Error updating seller info:', error);
        alert('회원정보 수정 중 오류가 발생했습니다.');
    }
});

// 3. 취소 버튼
document.getElementById('cancel-btn').addEventListener('click', () => {
    window.history.back();
}); 