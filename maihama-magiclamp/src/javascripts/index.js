import route from 'riot-route';
import Request from './request';
import Helper from './helper';
import Validator from './validator';
import Controller from './controller';
import Resource from './resource';

global.route = route;
global.observable = riot.observable();
global.request = new Request();
global.helper = new Helper();
global.validator = new Validator();
global.RC = new Resource;
new Controller();

import '../components/common/app';
import '../components/common/common-header';
import '../components/common/common-footer';
import '../components/common/pagenation';
import '../components/common/title-reactive';

import '../components/member/member-add';
import '../components/member/member-edit';
import '../components/member/member-list';
import '../components/member/member-purchase-list';

import '../components/product/product-list';
import '../components/product/product-detail';

import '../components/profile/profile';

import '../components/withdrawal/withdrawal';

import '../components/root/root';
import '../components/root/signin';
import '../components/root/signup';

import '../stylesheets/reset.css';
import '../stylesheets/common.css';
import '../stylesheets/individual.css';

riot.mount('*');