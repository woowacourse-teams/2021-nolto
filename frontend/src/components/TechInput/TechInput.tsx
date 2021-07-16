import React, { useRef, useState, useEffect } from 'react';

import useTechs from 'hooks/queries/useTechs';
import FormInput from 'components/@common/FormInput/FormInput';
import { Tech, ButtonStyle } from 'types';
import Styled, { TechButton } from './TechInput.styles';

interface Props {
  onUpdateTechs: (techs: Tech[]) => void;
}

const TechInput = ({ onUpdateTechs }: Props) => {
  const [isDropdownOpened, setDropdownOpened] = useState(false);
  const [currentTechIdx, setCurrentTechIdx] = useState(-1);
  const [selectedTechs, setSelectedTechs] = useState<Tech[]>([]);
  const [userInput, setUserInput] = useState('');

  const focusedOption = useRef(null);

  const { data: techs } = useTechs(userInput);

  useEffect(() => {
    if (techs?.length > 0) setDropdownOpened(true);
  }, [techs]);

  useEffect(() => {
    onUpdateTechs(selectedTechs);
  }, [selectedTechs]);

  useEffect(() => {
    focusedOption?.current?.scrollIntoView({ block: 'nearest' });
  }, [focusedOption?.current]);

  const moveFocusedOption = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'ArrowDown') {
      if (currentTechIdx === -1) setDropdownOpened(true);
      if (currentTechIdx < techs.length - 1) setCurrentTechIdx(currentTechIdx + 1);
    } else if (event.key === 'ArrowUp') {
      if (currentTechIdx >= 0) setCurrentTechIdx(currentTechIdx - 1);
    } else if (event.key === 'Enter') {
      event.preventDefault();

      if (!focusedOption.current) return;

      const { id, text } = focusedOption.current.dataset;
      const tech = {
        id: Number(id),
        text,
      };
      addTech(tech);
    }
  };

  const clickOption = (tech: Tech) => {
    addTech(tech);
  };

  const addTech = (tech: Tech) => {
    if (selectedTechs.some((selectedTech) => selectedTech.id === tech.id)) return;

    setSelectedTechs([...selectedTechs, tech]);
    setDropdownOpened(false);
    setCurrentTechIdx(-1);
    setUserInput('');
  };

  const deleteTech = (techId: number) => {
    setSelectedTechs(selectedTechs.filter((tech) => tech.id !== techId));
  };

  const handleInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;

    setUserInput(value);
  };

  return (
    <Styled.Root>
      <Styled.TechButtonsContainer>
        {selectedTechs.map((tech) => (
          <TechButton
            buttonStyle={ButtonStyle.SOLID}
            key={tech.id}
            onClick={() => {
              deleteTech(tech.id);
            }}
          >
            {tech.text}
          </TechButton>
        ))}
      </Styled.TechButtonsContainer>
      <FormInput value={userInput} onChange={handleInput} onKeyDown={moveFocusedOption} />
      {isDropdownOpened && (
        <Styled.Dropdown>
          {techs?.map((tech, idx) => (
            <Styled.TechOption
              key={tech.id}
              data-id={tech.id}
              data-text={tech.text}
              focused={idx === currentTechIdx}
              ref={idx === currentTechIdx ? focusedOption : null}
              onClick={() => clickOption(tech)}
            >
              {tech.text}
            </Styled.TechOption>
          ))}
        </Styled.Dropdown>
      )}
    </Styled.Root>
  );
};

export default TechInput;
