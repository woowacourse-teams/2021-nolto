import React from 'react';

import { ButtonStyle } from 'types';
import Styled, { TechButton } from './TechChips.styles';
import useTechTag from '../useTechTag';

interface Props {
  className?: string;
  reverse?: boolean;
}

const TechChips = ({ className, reverse = false }: Props) => {
  const techTag = useTechTag();

  const deleteTech = (techId: number) => {
    techTag.deleteTech(techId);
  };

  return (
    <Styled.Root className={className}>
      {techTag.techs.map((tech) => (
        <TechButton
          type="button"
          buttonStyle={ButtonStyle.SOLID}
          key={tech.id}
          reverse={reverse}
          onClick={() => deleteTech(tech.id)}
        >
          {tech.text}
          <Styled.DeleteMark width="10px" reverse={reverse} />
        </TechButton>
      ))}
    </Styled.Root>
  );
};

export default TechChips;
