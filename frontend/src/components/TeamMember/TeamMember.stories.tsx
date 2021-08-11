import React from 'react';

import TeamMember from './TeamMember';
import zigIcon from 'assets/team/zig.png';
import mickeyIcon from 'assets/team/mickey.png';

export default {
  title: 'components/TeamMember',
  component: TeamMember,
  argTypes: {},
};

const zig = {
  image: zigIcon,
  name: 'ZIG',
  introduction: '이 세상을 제정신으로 살기란 정말 어려운 일이야',
  github: 'https://github.com/zigsong',
  site: 'https://zigsong.github.io/',
};

const mickey = {
  image: mickeyIcon,
  name: 'MICKEY',
  introduction: '밥 먹고 초바 사왔습니다 ',
  github: 'https://github.com/0307kwon',
  site: 'https://velog.io/@0307kwon',
};

export const Zig = () => <TeamMember {...zig} />;

export const Mickey = () => <TeamMember {...mickey} reverse={true} />;
