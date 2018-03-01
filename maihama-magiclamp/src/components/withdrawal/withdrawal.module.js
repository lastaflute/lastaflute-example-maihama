import './withdrawal';

route('/withdrawal', () => {
  riot.mount('content', 'withdrawal');
});

riot.mixin('withdrawal', {
  api: {
    withdrawal: {
      reason: '/withdrawal/reason',
      done: '/withdrawal/done',
    }
  }
});