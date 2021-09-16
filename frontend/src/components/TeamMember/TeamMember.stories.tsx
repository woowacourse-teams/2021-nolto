import React from 'react';

import TeamMember from './TeamMember';
import zigPng from 'assets/team/zig.png';
import zigWebp from 'assets/team/zig.webp';
import mickeyPng from 'assets/team/mickey.png';
import mickeyWebp from 'assets/team/mickey.webp';

export default {
  title: 'components/TeamMember',
  component: TeamMember,
  argTypes: {},
};

const zig = {
  pngUrl: zigPng,
  webpUrl: zigWebp,
  name: 'ZIG',
  introduction: '이 세상을 제정신으로 살기란 정말 어려운 일이야',
  github: 'https://github.com/zigsong',
  site: 'https://zigsong.github.io/',
};

const mickey = {
  pngUrl: mickeyPng,
  webpUrl: mickeyWebp,
  name: 'MICKEY',
  introduction: '밥 먹고 초바 사왔습니다',
  github: 'https://github.com/0307kwon',
  site: 'https://velog.io/@0307kwon',
};

export const Zig = () => <TeamMember {...zig} />;

export const Mickey = () => <TeamMember {...mickey} reverse={true} />;
