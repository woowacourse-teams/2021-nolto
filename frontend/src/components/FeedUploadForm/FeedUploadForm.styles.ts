import styled from 'styled-components';

import TextArea from 'components/@common/TextArea/TextArea';
import TextButton from 'components/@common/TextButton/TextButton';
import Tooltip from 'components/@common/Tooltip/Tooltip';
import ToyboxIcon from 'assets/toybox.svg';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 30rem;
  margin-right: auto;
  margin-left: auto;
  padding-top: 7.75rem;

  & > form {
    width: 100%;
  }
`;

const TitleWrapper = styled.h2`
  margin-bottom: 3rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 5rem;
`;

const VerticalWrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  border-bottom: 2rem;
  width: 100%;

  & input,
  textarea {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

export const ContentTextArea = styled(TextArea)`
  height: 31.25rem;
`;

const InputsContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`;

const StretchWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  border-bottom: 2rem;
  width: 100%;

  & > .stretch-label {
    min-width: 7rem;
  }

  & input {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

const LevelWrapper = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 7rem;
`;

const SOSLabel = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: auto;
`;

export const StyledButton = styled(TextButton.Regular)`
  width: 12rem;
  height: 2.25rem;
  font-size: 1rem;
  border-radius: 7px;
  margin-top: 1rem;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 1.5rem;
  justify-content: center;
`;

const QuestionMark = styled.span``;

export const LevelTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 10%;
  left: 70%;
`;

export const SOSTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 15%;
  right: 105%;
`;

const InputCaption = styled.span`
  font-size: 0.75rem;
`;

export const Toybox = styled(ToyboxIcon)`
  position: absolute;
  height: 2rem;
  right: 0;
`;

export default {
  Root,
  TitleWrapper,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  LevelWrapper,
  SOSLabel,
  QuestionMark,
  ButtonsWrapper,
  InputCaption,
};
