import route from 'riot-route';
import Request from './request';
import Helper from './helper';
import Validator from './validator';

global.route = route;
global.observable = riot.observable();
global.request = new Request();
global.helper = new Helper();
global.validator = new Validator();

global.EVENT = {
  'route': {
    'change': 'onRounteChange'
  },
  'auth': {
    'check': 'onCheckSignin',
    'sign': 'onSign'
  },
  'pagenation': {
    'set': 'onPagenationSet'
  }
};

global.SESSION = {
  'member': {
    'info': 'memberInfo'
  },
  'auth': {
    'key': 'authKey'
  }
};