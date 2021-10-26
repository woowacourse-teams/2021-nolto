import IconButton from 'components/@common/IconButton/IconButton';
import React from 'react';

import Styled from './Footer.styles';
import GithubLogo from 'assets/githubLogo.svg';
import Mail from 'assets/mail.svg';
import Laptop from 'assets/laptop.svg';
import { FlexContainer } from 'commonStyles';

const NOLTO_GITHUB = 'https://github.com/woowacourse-teams/2021-nolto';
const NOLTO_MAIL = 'mailto:2021.nolto@gmail.com';
const NOLTO_INTRO_SITE =
  'https://sites.google.com/woowahan.com/wooteco-demo-3rd/%EB%86%80%ED%86%A0?authuser=0';

const Footer = () => {
  return (
    <Styled.Root>
      <FlexContainer gap="16px">
        <a href={NOLTO_GITHUB} target="_blank">
          <span className="visually-hidden">놀토 깃헙 페이지로 이동</span>
          <IconButton size="44px">
            <GithubLogo />
          </IconButton>
        </a>
        <a href={NOLTO_MAIL}>
          <span className="visually-hidden">놀토에 메일 보내기</span>
          <IconButton size="44px">
            <Mail />
          </IconButton>
        </a>
        <a href={NOLTO_INTRO_SITE} target="_blank">
          <span className="visually-hidden">놀토 소개 페이지로 이동</span>
          <IconButton size="44px">
            <Laptop />
          </IconButton>
        </a>
      </FlexContainer>
      <FlexContainer flexDirection="column" alignItems="center">
        <p>Sejin Kwon | Jieun Song | YeongSang Jo</p>
        <p>Minyoung Park | Jihye Shin | Eunsori Park</p>
      </FlexContainer>
      <p>© 2021 nolto</p>
    </Styled.Root>
  );
};

export default Footer;
