import './member-add';
import './member-edit';
import './member-list';
import './member-purchase-list';

route('/member/list/back', () => {
  riot.mount('content', 'member-list', { back: true });
});

route('/member/list..', () => {
  riot.mount('content', 'member-list', route.query());
});

route('/member/edit/*', (memberId) => {
  riot.mount('content', 'member-edit', { memberId });
});

route('/member/add', () => {
  riot.mount('content', 'member-add');
});

route('/member/purchase/*..', (memberId) => {
  const params = route.query();
  params.memberId = memberId;
  riot.mount('content', 'member-purchase-list', params);
});