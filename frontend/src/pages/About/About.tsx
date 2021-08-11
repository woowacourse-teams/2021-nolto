import React from 'react';

import Header from 'components/Header/Header';
import TeamMember from 'components/TeamMember/TeamMember';
import member from './members';
import Styled from './About.styles';

const About = () => {
  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.SectionTitle fontSize="1.75rem">We Make Nolto 🚀</Styled.SectionTitle>
        <Styled.MembersContainer>
          <TeamMember {...member.amazzi} />
          <TeamMember {...member.joel} reverse />
          <TeamMember {...member.pomo} />
          <TeamMember {...member.zig} reverse />
          <TeamMember {...member.mickey} />
          <TeamMember {...member.charlie} reverse />
        </Styled.MembersContainer>
      </Styled.Root>
    </>
  );
};

export default About;
