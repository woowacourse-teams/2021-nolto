import React, { useRef, useState, useEffect, InputHTMLAttributes } from 'react';

import useTechAutoComplete from 'hooks/queries/useTechAutoComplete';
import useQueryDebounce from 'hooks/@common/useQueryDebounce';
import useSnackBar from 'context/snackBar/useSnackBar';
import FormInput from 'components/@common/FormInput/FormInput';
import { Tech } from 'types';
import Styled from './TechInput.styles';
import useTechTag from '../useTechTag';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  onUpdateTechs: (techs: Tech[]) => void;
  className?: string;
}

const TechInput = ({ onUpdateTechs, className, ...options }: Props) => {
  const [isDropdownOpened, setDropdownOpened] = useState(false);
  const [currentTechIdx, setCurrentTechIdx] = useState(-1);
  const [searchInput, setSearchInput] = useState('');

  const focusedOption = useRef(null);

  const techTag = useTechTag();
  const snackbar = useSnackBar();
  const debouncedSearchInput = useQueryDebounce(searchInput, 200);
  const { data: techs } = useTechAutoComplete({
    autoComplete: debouncedSearchInput,
    errorHandler: (error) => snackbar.addSnackBar('error', error.message),
  });

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

  const addTech = (tech: Tech) => {
    if (techTag.techs.some((selectedTech) => selectedTech.id === tech.id)) return;

    techTag.pushTech(tech);
    setDropdownOpened(false);
    setCurrentTechIdx(-1);
    setSearchInput('');
  };

  const handleInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;

    setSearchInput(value);
  };

  useEffect(() => {
    if (techs?.length > 0) setDropdownOpened(true);
  }, [techs]);

  useEffect(() => {
    onUpdateTechs(techTag.techs);
  }, [techTag.techs]);

  useEffect(() => {
    focusedOption?.current?.scrollIntoView({ block: 'nearest' });
  }, [focusedOption?.current]);

  return (
    <Styled.Root className={className}>
      <FormInput
        value={searchInput}
        onChange={handleInput}
        onKeyDown={moveFocusedOption}
        {...options}
      />
      {isDropdownOpened && (
        <Styled.Dropdown>
          {techs?.map((tech, idx) => (
            <Styled.TechOption
              key={tech.id}
              data-id={tech.id}
              data-text={tech.text}
              focused={idx === currentTechIdx}
              ref={idx === currentTechIdx ? focusedOption : null}
              onClick={() => addTech(tech)}
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
